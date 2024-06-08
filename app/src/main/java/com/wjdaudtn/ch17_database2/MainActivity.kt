package com.wjdaudtn.ch17_database2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch17_database.DBHelper
import com.example.ch17_database.MyAdapter
import com.wjdaudtn.ch17_database2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datas = mutableListOf<String>()
        functionDB()
        functionRecyclerView()

        binding.btnMain.setOnClickListener(MysetOnClickListener)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId===R.id.menu_main_setting){
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    private val MysetOnClickListener: View.OnClickListener = (object : View.OnClickListener{
        override fun onClick(v: View?) {
            val intent:Intent = Intent(this@MainActivity,AddActivity::class.java)
            requestLauncher.launch(intent)
        }
    })

    val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        it.data!!.getStringExtra("result")?.let{
            datas?.add(it)
            adapter.notifyDataSetChanged()
        }
    }
    private fun functionDB(){
        val db= DBHelper(this).readableDatabase
        val cursor = db.rawQuery("select * from TODO_TB", null)
        cursor.run{
            while(moveToNext()){
                datas?.add(cursor.getString(1))
            }
        }
        db.close()
    }
    private fun functionRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMain.layoutManager=layoutManager
        adapter= MyAdapter(datas)
        binding.recyclerViewMain.adapter=adapter
        binding.recyclerViewMain.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }
}