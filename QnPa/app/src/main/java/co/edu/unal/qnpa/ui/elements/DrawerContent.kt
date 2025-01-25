package co.edu.unal.qnpa.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {}
){
    Surface(
        modifier = Modifier
            .width(200.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Quien Pa",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider()
            Spacer(modifier = Modifier.height(4.dp))
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Account Icon",
                        modifier = Modifier
                            .size(27.dp)
                    )
                },
                label = {
                    Text(text = "Perfil")
                },
                selected = false,
                onClick = {
                    onProfileClick()
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Create,
                        contentDescription = "Edit Icon",
                        modifier = Modifier.size(27.dp)
                    )
                },
                label = {
                    Text(text = "Editar Perfil")
                },
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(4.dp))
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Config Icon",
                        modifier = Modifier.size(27.dp)
                    )
                },
                label = {
                    Text(text = "Configuración")
                },
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}