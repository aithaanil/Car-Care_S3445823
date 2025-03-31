package uk.ac.tees.mad.carcare.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.carcare.R
import uk.ac.tees.mad.carcare.model.dataclass.firebase.AuthResult
import uk.ac.tees.mad.carcare.model.dataclass.firebase.UserDetails
import uk.ac.tees.mad.carcare.ui.navigation.SubGraph


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    openAndPopUp: (Any, Any) -> Unit,
    navigate: (Any) -> Unit,
    viewmodel: HomeScreenViewModel = koinViewModel<HomeScreenViewModel>()
) {
    val userDetails by viewmodel.userDetails.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // icons to mimic drawer destinations
    val items = listOf(
        Icons.Default.Dashboard,
        Icons.Default.BookOnline,
        Icons.Default.History,
        Icons.Default.ManageAccounts,
    )
    val selectedItem = remember { mutableStateOf(items[0]) }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            TopAppBar(
                title = {
                Text(
                    stringResource(R.string.app_name),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    viewmodel.logOut()
                    openAndPopUp(SubGraph.AuthGraph, SubGraph.HomeGraph)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout Button"
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        }) { innerPadding ->
        ModalNavigationDrawer(
            drawerState = drawerState, modifier = modifier.padding(innerPadding), drawerContent = {
                ModalDrawerSheet(drawerState) {
                    Column {
                        items.forEach { item ->
                            NavigationDrawerItem(
                                icon = { Icon(item, contentDescription = null) },
                                label = { Text(item.name.substringAfterLast(".")) },
                                selected = item == selectedItem.value,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    selectedItem.value = item
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }
            }) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    UserDetailsCard(userDetails, viewmodel)
                }
                item {
                    QuickActionsCard(viewmodel, navigate, modifier)
                }
            }

        }
    }
}

@Composable
fun UserDetailsCard(userDetails: AuthResult<UserDetails>, viewmodel: HomeScreenViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            when (userDetails) {
                is AuthResult.Success -> {
                    Text("Welcome, ${if (userDetails.data.displayName.isNullOrEmpty()) "User" else userDetails.data.displayName}!")
                    Text("Email: ${userDetails.data.email}")
                }

                is AuthResult.Error -> {
                    Text("Error: ${userDetails.exception.message}")
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                            //retry fetching user details
                            viewmodel.fetchUserDetails()
                        }) {
                        Text("Retry?")
                    }
                }

                is AuthResult.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(50.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionsCard(
    viewmodel: HomeScreenViewModel, navigate: (Any) -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quick Actions", style = MaterialTheme.typography.headlineMedium
            )
            // Quick Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CarCareButton(
                    modifier = Modifier.weight(1f), label = "Book a Service", onClick = {
                        //navigate(Dest.BookingScreen)
                    })

                CarCareButton(
                    modifier = Modifier.weight(1f), label = "View History", onClick = {
                        //navigate(Dest.AppointmentHistoryScreen)
                    })

                CarCareButton(
                    modifier = Modifier.weight(1f), label = "Manage Profile", onClick = {
                        //navigate(Dest.Profile)
                    })
            }
        }
    }
}

@Composable
fun CarCareButton(
    modifier: Modifier = Modifier, label: String, onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Text(text = label, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}