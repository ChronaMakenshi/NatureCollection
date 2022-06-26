package fr.chrona.naturecollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.chrona.naturecollection.fragments.AddPlantFragment
import fr.chrona.naturecollection.fragments.CollectionFragment
import fr.chrona.naturecollection.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment(this), R.string.home_page_title)

        // importer la bottomnavigationview
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_page -> {
                    loadFragment(HomeFragment(this), R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener  true
                }
                R.id.collection__page -> {
                    loadFragment(CollectionFragment(this), R.string.collection_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.add_plant_page -> {
                    loadFragment(AddPlantFragment(this), R.string.add_plant_page_title)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }
    }

      private fun loadFragment(fragment: Fragment, string: Int) {

          // Charger notre PlantRepository
          val repo = PlantRepository()

          // actualiser le titre de la page
          findViewById<TextView>(R.id.page_title).text = resources.getString(string)

          // mettre à jour la liste de plants
          repo.updateData {
              // injecter le fragment dans notre boite (fragment_container)
              val transaction = supportFragmentManager.beginTransaction()
              transaction.replace(R.id.fragment_container, fragment)
              transaction.addToBackStack(null)
              transaction.commit()
          }

      }
    }
