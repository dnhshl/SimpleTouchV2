package com.example.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.main.databinding.ActivityMainBinding
import com.example.main.model.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val LISTSIZE = "listsize"
    private val LISTITEM_TIME = "time_"
    private val LISTITEM_NAME = "name_"
    private val NICKNAME = "nickname"

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readSharedPreferences()

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_nickname -> {
                Log.i(TAG, "Menu Nickname selected")
                showDialogEditNickname()
                true
            }
            R.id.action_highscore -> {
                Log.i(TAG, "Menu Highscore selected")
                showHighscoreDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showDialogEditNickname() {
        val editTextView = EditText(this)
        this?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.dialog_nickname_title)
                .setView(editTextView)
                .setNeutralButton(R.string.dialog_cancel) { dialog, which ->
                }
                .setPositiveButton(R.string.dialog_OK) { dialog, which ->
                    vm.nickname = editTextView.text.toString()
                }
                .show()
        }
    }

    private fun showHighscoreDialog() {
        // für den ListView benötigen wir eine Liste mit Strings
        val highScoreList = ArrayList<String>()
        // wir leiten diese Liste aus der HighScore Liste im
        // ViewModel ab
        vm.highscoreList.forEach {item ->
            highScoreList.add("${item.nickname}: ${item.time.toString()}")
        }
        // wenn es noch gar keinen Eintrag gibt, dann Infotext in Liste eintragen
        if (highScoreList.size == 0) highScoreList.add(getString(R.string.highscore_no_entry))
        val listView = ListView(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, highScoreList)
        listView.adapter = adapter

        // ListView im Dialog einbinden
        this?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.dialog_highscore_title)
                .setView(listView)
                .setPositiveButton(R.string.dialog_OK) { dialog, which ->
                    // do nothing
                }
                .show()
        }
    }

    private fun writeSharedPreferences() {

        val sp = getPreferences(Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putInt(LISTSIZE, vm.highscoreList.size)
        for (i in 0 until vm.highscoreList.size) {
            edit.putString("$LISTITEM_NAME$i", vm.highscoreList.get(i).nickname)
            edit.putLong("$LISTITEM_TIME$i", vm.highscoreList.get(i).time)
        }
        edit.putString("$NICKNAME", vm.nickname)
        edit.commit()
        Log.i(TAG, "writeSharedPreferences Anzahl ${vm.highscoreList.size}")
    }

    private fun readSharedPreferences() {

        val sp = getPreferences(Context.MODE_PRIVATE)
        val anzahl = sp.getInt(LISTSIZE, 0)
        for (i in 0 until anzahl) {
            val nickname = sp.getString("$LISTITEM_NAME$i", "").toString()
            val time = sp.getLong("$LISTITEM_TIME$i", 0)
            vm.highscoreList.add(vm.HighScoreListItem(nickname, time))
        }
        vm.nickname = sp.getString(NICKNAME, "anonymous").toString()
        Log.i(TAG, "readSharedPreferences Anzahl $anzahl")
    }

    override fun onPause() {
        super.onPause()
        writeSharedPreferences()
    }
}