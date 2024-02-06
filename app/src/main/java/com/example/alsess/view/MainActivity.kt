package com.example.alsess.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.alsess.R
import com.example.alsess.databinding.ActivityMainBinding
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activityMainMavHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(
            viewBinding.activityMainBottomNavigationView,
            navHostFragment.navController
        )

        //BattomNavigation menu disappears when you enter the product detail page
        navHostFragment.navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.productDetailFragment) {
                viewBinding.activityMainBottomNavigationView.visibility = View.GONE
            } else {
                basketBedge()
                viewBinding.activityMainBottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    //The number of products added to the cart is displayed as a bottom notification.
    fun basketBedge() {
        val basketSQLiteDataHelper = BasketSqliteDataHelper(this)
        val basketSqliteList = BasketSqliteDao().getAllBaskets(basketSQLiteDataHelper)
        val basketBadge =
            viewBinding.activityMainBottomNavigationView.getOrCreateBadge(R.id.basketFragment)
        basketBadge.number = basketSqliteList.size
        basketBadge.isVisible = true
        if (basketSqliteList.size == 0) {
            basketBadge.isVisible = false
        }
    }
}
