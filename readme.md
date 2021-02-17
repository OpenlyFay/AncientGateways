# Ancient Gateways: Patched
This is a fork of the Ancient Gateways Mod [[ *[curseforge](https://www.curseforge.com/minecraft/mc-mods/ancient-gateways), [github](https://github.com/OpenlyFay/AncientGateways)* ]]

This fork seeks to fix a few bugs, and add a few Quality of life features

## Fixes and Features Added
* Redstone can now activate the gateway
	![gif](.readme/redstone-activation.gif)
* You can now configure how long the gateway remains open!
	![gif](.readme/delayed-deactivation.gif)
* Gateways no longer open when the receiving end was obstructed
	* Old behavior
	![img](https://i.imgur.com/gpsLmGv.png)
	* New behavior
		* Cant show by image, however, basically we fixed the issue where the receiving gate, did not run a Integrety Check, so matter how the receving gate was physically set. So the receving gate could receive players despite being obstructed, this is clearly not something i think the developer of the original mod had intended at all.
		* So we made it run the check on the receiving gate, and if it is obstructed, simply do not activate at all