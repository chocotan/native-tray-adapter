package tray;

import java.awt.PopupMenu;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import jni.utils.JniUtils;
import tray.linux.NativeLinuxTray;

public class LinuxNativeTrayAdapter implements SystemTrayAdapter {
	private static final NativeLinuxTray nativeTray;
	static Map<Integer,LinuxTrayIconAdapter> trayIconInstances = new LinkedHashMap<Integer,LinuxTrayIconAdapter>();
	static {
		JniUtils.loadLibrary("linuxtray_x64");
		nativeTray = new NativeLinuxTray();
	}

	@Override
	public TrayIconAdapter createAndAddTrayIcon(URL imageURL, String tooltip,
			PopupMenu popup) {
		LinuxTrayIconAdapter linuxTrayIconAdapter = new LinuxTrayIconAdapter(
				nativeTray, imageURL, tooltip, popup);
		trayIconInstances.put(linuxTrayIconAdapter.getNativeId(), linuxTrayIconAdapter);
		return linuxTrayIconAdapter;
	}

	public static LinuxTrayIconAdapter getLinuxTrayIconAdapter(int nativeId) {
		return trayIconInstances.get(nativeId);
	}

	@Override
	public void remove(TrayIconAdapter trayIcon) {
		LinuxTrayIconAdapter linuxTrayIconAdapter = (LinuxTrayIconAdapter)trayIcon;
		trayIconInstances.remove(linuxTrayIconAdapter.getNativeId());
		linuxTrayIconAdapter.removeMe();
	}
}