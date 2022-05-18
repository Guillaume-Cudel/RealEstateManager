package com.guillaume.project9.ui

import com.guillaume.project9.model.Property

interface Communicator {
    fun passData(property: Property)
}