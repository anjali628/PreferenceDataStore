package com.example.preferencedatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.preferencedatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataStore:DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStore=createDataStore(name = "settings")

        binding.btnSave.setOnClickListener {

            lifecycleScope.launch {

                save(
                    binding.etSaveKey.text.toString(),
                    binding.etSaveValue.text.toString()
                )

            }
            Toast.makeText(applicationContext,"saved",Toast.LENGTH_SHORT).show()
        }

        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value=read(binding.etReadkey.text.toString())
                binding.tvReadValue.text=value
            }
        }

    }

    private suspend fun save(key:String,value:String)
    {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit {
            it[dataStoreKey]=value

        }

        Toast.makeText(applicationContext,"saved "+key+" "+value,Toast.LENGTH_SHORT).show()

    }

    private suspend fun read(key:String):String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences=dataStore.data.first()
        return preferences[dataStoreKey]
    }

}