package com.example.alsess

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.home_fragment_row.*

class HomePage : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.navController)
        // Detay fragmante girince battom navigation bar kaybolur
        navHostFragment.navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.productDetailFragment) {
                bottomNavigation.visibility = View.GONE
            }else{
                // sepete ürün eklendiğinde eklenen farklı ürün sayısına göre badge bilriimi yazılır
                val badge = bottomNavigation.getOrCreateBadge(R.id.basketFragment)
                val basketDataHelper = BasketSqliteDataHelper(this@HomePage)
                val basketList = BasketSqliteDao().getAllBaskets(basketDataHelper)
                badge.number = basketList.size
                badge.isVisible = true
                if(basketList.size == 0){
                    badge.isVisible = false
                }
                bottomNavigation.visibility = View.VISIBLE
            }
        }
    }
}