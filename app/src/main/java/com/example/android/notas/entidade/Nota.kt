package com.example.android.notas.entidade

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nota_database")
data class Nota(@PrimaryKey @ColumnInfo(name = "nota") val nota: String)
