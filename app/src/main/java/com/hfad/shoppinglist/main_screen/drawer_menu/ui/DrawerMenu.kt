package com.hfad.shoppinglist.main_screen.drawer_menu.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.shoppinglist.login_screen.data.UserData
import com.hfad.shoppinglist.ui.theme.BlueLight
import com.hfad.shoppinglist.ui.theme.GrayLight3
import com.hfad.shoppinglist.ui.theme.Purple80
import com.hfad.shoppinglist.ui.theme.PurpleGrey

@Composable
fun DrawerMenu(
    userData: UserData,
    onItemClick: (Int) -> Unit
) {
    val items = listOf(
        "About",
        "Delete account",
        "Sign Out"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight()
            .background(PurpleGrey),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DrawerHeader(userData = userData)
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(items) { index, itemTitle ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = itemTitle,
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .wrapContentWidth()
                            )
                        },
                        selected = false,
                        onClick = {
                            onItemClick(index)
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp)
                            .height(1.dp)
                            .background(GrayLight3)
                    )
                }
            }

        }
        Text(
            text = "Version - 1.0.0",
            color = BlueLight,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(bottom = 10.dp)
        )

    }
}