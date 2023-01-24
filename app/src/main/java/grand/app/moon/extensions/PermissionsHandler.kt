package grand.app.moon.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Size
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError
import java.lang.ref.WeakReference

abstract class ListenerOfPermissionsHandler : PermissionsHandler.Listener

open class PermissionsHandler private constructor(
	lifecycle: Lifecycle,
	context: Context,
	host: Any,
	private val permissions: List<String>,
	listener: Listener,
) : DefaultLifecycleObserver {

	constructor(fragment: Fragment, lifecycle: Lifecycle, context: Context, permissions: List<String>, listener: Listener) : this(
		lifecycle, context, fragment, permissions, listener
	)

	constructor(activity: FragmentActivity, lifecycle: Lifecycle, context: Context, permissions: List<String>, listener: Listener) : this(
		lifecycle, context, activity, permissions, listener
	)

	private val weakRefLifecycle = WeakReference(lifecycle)
	private val weakRefContext = WeakReference(context)
	val weakRefHost = WeakReference(host)
	private var weakRefListener = WeakReference(listener)

	init {
		lifecycle.addObserver(this)
	}

	private val activityResultLauncherPermissions = host.registerForActivityResult(
		ActivityResultContracts.RequestMultiplePermissions()
	) { permissions ->
		onActivityPermissionsLauncherResult(permissions)
	}

	private val activityResultPermissionsSystemSettings = host.registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		checkOnPermissions(weakRefContext.get() ?: return@registerForActivityResult)
	}

	private fun onActivityPermissionsLauncherResult(permissions: Map<String, Boolean>) {
		val activity = weakRefHost.get().getActivityOrNull()

		when {
			this.permissions.all { permissions[it] == true } -> {
				weakRefListener.get()?.onAllPermissionsAccepted()
			}
			this.permissions.any { permissions[it] == true } -> {
				weakRefListener.get()?.onSubsetPermissionsAccepted(permissions)
			}
			activity != null -> {
				val rationaleList = this.permissions.filter {
					weakRefHost.get().shouldShowRequestPermissionRationale(it)
				}

				if (rationaleList.isNotEmpty()) {
					weakRefListener.get()?.onShouldShowRationale(this, rationaleList)
				}else {
					weakRefListener.get()?.onDenyPermissions(this)
				}
			}
		}
	}

	fun requestPermissions() {
		activityResultLauncherPermissions.launchSafely(
			weakRefContext.get(),
			this.permissions.toTypedArray()
		)
	}

	fun actOnAllPermissionsAcceptedOrRequestPermissions() {
		val context = weakRefContext.get() ?: return
		MyLogger.e("aaaaaaaaaa -> on all weakRefListener.get() ${weakRefListener.get()}")
		if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
			weakRefListener.get()?.onAllPermissionsAccepted()
		}else {
			activityResultLauncherPermissions.launchSafely(
				weakRefContext.get(),
				this.permissions.toTypedArray()
			)
		}
	}

	private fun checkOnPermissions(context: Context) {
		onActivityPermissionsLauncherResult(
			permissions.associateWith {
				ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
			}
		)
	}

	override fun onDestroy(owner: LifecycleOwner) {
		weakRefLifecycle.get()?.removeObserver(this)
	}

	interface Listener {

		fun onAllPermissionsAccepted()

		fun onSubsetPermissionsAccepted(permissions: Map<String, Boolean>) {}

		/** @param list contains list of permissions which returns `true` to [shouldShowRequestPermissionRationale] fun */
		fun onShouldShowRationale(permissionsHandler: PermissionsHandler, @Size(min = 1) list: List<String>) {
			permissionsHandler.weakRefHost.get().getActivityOrNull()?.apply {
				showAlertDialog(
					getString(R.string.allow_location_permission),
					getString(R.string.this_app_need_allow_location),
					onDismissListener = {
						permissionsHandler.weakRefContext.get()?.also { context ->
							context.showError(context.getString(R.string.you_didn_t_accept_permission))
						}
					}
				) {
					permissionsHandler.activityResultLauncherPermissions.launchSafely(
						permissionsHandler.weakRefContext.get(),
						permissionsHandler.permissions.toTypedArray()
					)
				}
			}
		}

		fun onDenyPermissions(permissionsHandler: PermissionsHandler) {
			permissionsHandler.weakRefHost.get().getActivityOrNull()?.apply {
				showAlertDialog(
					getString(R.string.change_permission_in_settings_of_device),
					getString(R.string.this_app_need_allow_location),
					onDismissListener = {
						permissionsHandler.weakRefContext.get()?.also { context ->
							context.showError(context.getString(R.string.you_didn_t_accept_permission))
						}
					}
				) {
					val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
						it.data = Uri.fromParts("package", packageName, null)
					}

					permissionsHandler.activityResultPermissionsSystemSettings.launchSafely(
						permissionsHandler.weakRefContext.get(),
						intent
					)
				}
			}
		}

	}

}
