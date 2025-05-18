package io.github.kdroidfilter.database.generator

import java.nio.file.Paths

fun main() {
    val projectDir = Paths.get("").toAbsolutePath()
    val root       = projectDir.parent.resolve("app-policies")
    val outputDb   = Paths.get("build", "policies.db")

    SQLitePolicyExporter.exportAll(root, outputDb)
}
