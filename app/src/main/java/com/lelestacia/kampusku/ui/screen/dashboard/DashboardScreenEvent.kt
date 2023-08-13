package com.lelestacia.kampusku.ui.screen.dashboard

sealed class DashboardScreenEvent {
    object OnShowDataClicked : DashboardScreenEvent()
    object OnInputDataClicked : DashboardScreenEvent()
    object OnInformationClicked : DashboardScreenEvent()
}
