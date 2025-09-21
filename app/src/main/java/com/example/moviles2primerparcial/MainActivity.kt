package com.example.moviles2primerparcial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviles2primerparcial.ui.breedslist.BreedsListFragment

/**
 * Hosts a single fragment container. On first creation, it shows the breeds list.
 * Fragment transactions are managed manually with FragmentManager.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Insert initial fragment only once to avoid duplicates on configuration changes.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BreedsListFragment())
                .commit()
        }
    }
}
