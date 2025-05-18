package io.github.kdroidfilter.database.generator

import io.github.kdroidfilter.database.core.policies.AppPolicy
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager

object SQLitePolicyExporter {
    // Reuse the same JSON configuration as PolicyRepository
    private val json = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
        encodeDefaults = false
        prettyPrint = false
        serializersModule = PolicyRepository.json.serializersModule
    }

    fun exportAll(root: Path, outputDb: Path) {
        // Load the SQLite driver
        Class.forName("org.sqlite.JDBC")

        // Creation / opening of the database
        val url = "jdbc:sqlite:${outputDb.toAbsolutePath()}"
        DriverManager.getConnection(url).use { conn ->
            conn.autoCommit = false
            createTable(conn)

            val insertSql = """
                INSERT OR REPLACE INTO policies(package_name, data) 
                VALUES(?, ?)
            """.trimIndent()

            conn.prepareStatement(insertSql).use { ps ->
                PolicyRepository.loadAll(root).forEach { policy ->
                    val jsonStr = json.encodeToString(AppPolicy.serializer(), policy)
                    ps.setString(1, policy.packageName)
                    ps.setString(2, jsonStr)
                    ps.addBatch()
                }
                ps.executeBatch()
            }

            conn.commit()
            println("✅ Export ${PolicyRepository.loadAll(root).size} policies in $outputDb")
        }
    }

    private fun createTable(conn: Connection) {
        conn.createStatement().use { stmt ->
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS policies (
                  package_name TEXT PRIMARY KEY,
                  data         TEXT NOT NULL
                )
            """.trimIndent())
        }
    }
}
