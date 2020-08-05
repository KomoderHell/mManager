package com.komodor.mmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_1 -> {
                    toolbar.title = "Notes"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrame, NotesFragment()).commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.item_2 -> Toast.makeText(
                    applicationContext,
                    "item 2 setected",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.item_3 -> Toast.makeText(
                    applicationContext,
                    "item 3 setected",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.item_4 -> Toast.makeText(
                    applicationContext,
                    "item 4 setected",
                    Toast.LENGTH_SHORT
                ).show()

            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
