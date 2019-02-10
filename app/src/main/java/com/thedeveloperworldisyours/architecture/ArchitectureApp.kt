package com.thedeveloperworldisyours.architecture

import android.app.Application
import android.support.constraint.Constraints

class ArchitectureApp : Application() {
        /**
         * onCreate is called before the first screen is shown to the user.
         *
         * Use it to setup any background tasks, running expensive setup operations in a background
         * thread to avoid delaying app start.
         */
        override fun onCreate() {
            super.onCreate()
            setupWorkManagerJob()
        }

        /**
         * Setup WorkManager background job to 'fetch' new network data daily.
         */
        private fun setupWorkManagerJob() {
            // Use constraints to require the work only run when the device is charging and the
            // network is unmetered
        }
}