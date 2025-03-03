package co.edu.unal.qnpa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import co.edu.unal.qnpa.pages.CreatePage
import co.edu.unal.qnpa.pages.GpsPage
import co.edu.unal.qnpa.pages.HomePage
import co.edu.unal.qnpa.ui.elements.DrawerContent
import co.edu.unal.qnpa.ui.elements.NavItem
import co.edu.unal.qnpa.ui.elements.TopBar
import co.edu.unal.qnpa.viewmodels.HomeScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateToSearch: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateToCategories: () -> Unit = {},
    navigateToEditProfile: () -> Unit = {},
    navigateToActivityDetails: (String) -> Unit = {},
    sessionManager: SessionManager,
    viewModel: HomeScreenViewModel = viewModel() // Agrega el ViewModel
) {
    val userId = sessionManager.getUserId()

    // Cargar la imagen del usuario cuando se inicia la pantalla
    LaunchedEffect(userId) {
        userId?.let { viewModel.loadUserImage(it) }
    }

    val navItemLists = listOf(
        NavItem(
            label = "Inicio",
            icon = Icons.Default.Home
        ),
        NavItem(
            label = "GPS",
            icon = Icons.Default.LocationOn
        ),
        NavItem(
            label = "Parche",
            icon = Icons.Default.AddCircle
        ),
    )
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        bottomBar = {
            NavigationBar {
                navItemLists.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        },
                        label = {
                            Text(text = navItem.label)
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedItem,
            navigateToSearch = navigateToSearch,
            navigateToProfile = navigateToProfile,
            navigateToLogin = navigateToLogin,
            navigateToCategories = navigateToCategories,
            navigateToEditProfile = navigateToEditProfile,
            navigateToActivityDetails = navigateToActivityDetails,
            sessionManager = sessionManager,
            viewModel = viewModel // Pasar el ViewModel a ContentScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    navigateToSearch: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateToCategories: () -> Unit = {},
    navigateToEditProfile: () -> Unit = {},
    navigateToActivityDetails: (String) -> Unit = {},
    sessionManager: SessionManager,
    viewModel: HomeScreenViewModel // Recibir el ViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    viewModel = viewModel, // Pasar el ViewModel al DrawerContent
                    onProfileClick = {
                        navigateToProfile()
                    },
                    onLogoutClick = {
                        sessionManager.logout()
                        navigateToLogin()
                    },
                    onCategoriesClick = {
                        navigateToCategories()
                    },
                    onProfileEditClick = {
                        navigateToEditProfile()
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopBar(
                    scrollBehavior = scrollBehavior,
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    onSearchClick = {
                        navigateToSearch()
                    },
                    onProfileClick = {
                        navigateToProfile()
                    },
                    viewModel = viewModel // Pasar el ViewModel al TopBar
                )
            }
        ) { innerPadding ->
            when (selectedIndex) {
                0 -> HomePage(
                    paddingValues = innerPadding,
                    navigateToActivityDetails = navigateToActivityDetails
                )
                1 -> GpsPage(
                    paddingValues = innerPadding,
                    navigateToActivity = navigateToActivityDetails
                )
                2 -> CreatePage(
                    paddingValues = innerPadding,
                    sessionManager = sessionManager,
                )
            }
        }
    }
}


