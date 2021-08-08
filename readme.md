# Ancient Gateways: Patched
This is a fork of the Ancient Gateways Mod [[ *[curseforge](https://www.curseforge.com/minecraft/mc-mods/ancient-gateways), [github](https://github.com/OpenlyFay/AncientGateways)* ]]

This fork seeks to fix a few bugs, and add a few Quality of life features

[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-908a85?logo=gitpod)](https://gitpod.io/#https://github.com/Merith-TK/AncientGateways)

## Fixes and Features Added
* Redstone can now activate the gateway
	![gif](.readme/redstone-activation.gif)
* You can now configure how long the gateway remains open!
	* The delay also works across dimensions, so if you open a portal in the nether and have it on a delay with comparators, the overworld portal will stay open as long as the nether one is open
	* If you try to power the destination gateway whilst the origin gateway is still powered, it will restart the countdown until the portal is closed 
	![gif](.readme/delayed-deactivation.gif)
* Gateways no longer open when the receiving end was obstructed
	* Old behavior
	![img](https://i.imgur.com/gpsLmGv.png)
	* New behavior
		* We fixed a bug where when we power the origin gateway it wasn't checking to make sure the destination gateway is intact before creating the portal
		* Now both gateways must be intact in order for portals to be opened
