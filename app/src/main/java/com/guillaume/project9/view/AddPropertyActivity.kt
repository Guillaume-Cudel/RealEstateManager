package com.guillaume.project9.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityAddPropertyBinding

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPropertyBinding

    var kindResult: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEstateAgentInputText()
        setKindProperty()

        binding.addPropertyValidateButton.setOnClickListener {
            recovePropertyData()
        }
    }


    private fun setKindProperty(){

        binding.addPropertyKindHouse.setOnClickListener {
            setColorKindProperty(0)
            recoveKindProperty(0)
        }
        binding.addPropertyKindGround.setOnClickListener {
            setColorKindProperty(1)
            recoveKindProperty(1)
        }
        binding.addPropertyKindApartment.setOnClickListener {
            setColorKindProperty(2)
            recoveKindProperty(2)
        }
        binding.addPropertyKindParking.setOnClickListener {
            setColorKindProperty(3)
            recoveKindProperty(3)
        }
        binding.addPropertyKindOther.setOnClickListener {
            setColorKindProperty(4)
            recoveKindProperty(4)
        }
    }

    private fun setColorKindProperty(kind: Int){
        binding.addPropertyKindHouse.clearColorFilter()
        binding.addPropertyKindHouse.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindGround.clearColorFilter()
        binding.addPropertyKindGround.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindApartment.clearColorFilter()
        binding.addPropertyKindApartment.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindParking.clearColorFilter()
        binding.addPropertyKindParking.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindOther.clearColorFilter()
        binding.addPropertyKindOther.setBackgroundResource(R.drawable.round)

        if (kind == 0) {
            binding.addPropertyKindHouse.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
            binding.addPropertyKindHouse.setBackgroundColor(resources.getColor(R.color.white))
        }
        if(kind == 1){
            binding.addPropertyKindGround.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
            binding.addPropertyKindGround.setBackgroundColor(resources.getColor(R.color.white))
        }
        if(kind == 2){
            binding.addPropertyKindApartment.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
            binding.addPropertyKindApartment.setBackgroundColor(resources.getColor(R.color.white))
        }
        if(kind == 3){
            binding.addPropertyKindParking.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
            binding.addPropertyKindParking.setBackgroundColor(resources.getColor(R.color.white))
        }
        if(kind == 4){
            binding.addPropertyKindOther.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
            binding.addPropertyKindOther.setBackgroundColor(resources.getColor(R.color.white))
        }
    }

    private fun recoveKindProperty(kind: Int){
        when(kind){
            0 -> kindResult = "House"
            1 -> kindResult = "Ground"
            2 -> kindResult = "Apartment"
            3 -> kindResult = "Parking"
            4 -> kindResult = "Other"
        }
    }

    private fun recovePropertyData(){
        // todo continu to recove all data
        binding.addPropertyPriceEdit.editableText.toString()

    }


    private fun configureEstateAgentInputText(){
        val estateAgents = resources.getStringArray(R.array.Estate_agents)
        val arrayAdapter = ArrayAdapter(this, R.layout.estate_agent_list_items, estateAgents)
        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.add_property_estate_agent_text)
        autoCompleteTextView.setAdapter(arrayAdapter)
    }


}