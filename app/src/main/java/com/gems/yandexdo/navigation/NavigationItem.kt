package com.gems.yandexdo.navigation

sealed class NavigationItem (val route:String){
    data object Main : NavigationItem("main")
    data object Task : NavigationItem("task")
}