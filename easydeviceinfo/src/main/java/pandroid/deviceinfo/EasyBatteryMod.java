/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pandroid.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * EasyBattery Mod Class
 */
@BatteryHealth
public class EasyBatteryMod {
  private final Context context;

  /**
   * Instantiates a new Easy battery mod.
   *
   * @param context
   *     the context
   */
  public EasyBatteryMod(final Context context) {
    this.context = context;
  }

  /**
   * Gets battery percentage.
   *
   * @return the battery percentage
   */
  public final int getBatteryPercentage() {
    int percentage = 0;
    Intent batteryStatus = getBatteryStatusIntent();
    if (batteryStatus != null) {
      int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
      int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
      percentage = (int) ((level / (float) scale) * 100);
    }

    return percentage;
  }

  private Intent getBatteryStatusIntent() {
    IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    return context.registerReceiver(null, batFilter);
  }

  /**
   * Is device charging boolean.
   *
   * @return is battery charging boolean
   */
  public final boolean isDeviceCharging() {
    Intent batteryStatus = getBatteryStatusIntent();
    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    return status == BatteryManager.BATTERY_STATUS_CHARGING
        || status == BatteryManager.BATTERY_STATUS_FULL;
  }

  /**
   * Gets battery health.
   *
   * @return the battery health
   */
  @BatteryHealth
  public final int getBatteryHealth() {
    int health = BatteryHealth.HAVING_ISSUES;
    Intent batteryStatus = getBatteryStatusIntent();
    if (batteryStatus != null) {
      health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
      if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
        health = BatteryHealth.GOOD;
      } else {
        health = BatteryHealth.HAVING_ISSUES;
      }
    }
    return health;
  }

  /**
   * Gets battery technology.
   *
   * @return the battery technology
   */
  public final String getBatteryTechnology() {
    return CheckValidityUtil.checkValidData(
        getBatteryStatusIntent().getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
  }

  /**
   * Gets battery temprature.
   *
   * @return the battery temprature
   */
  public final float getBatteryTemperature() {
    float temp = 0.0f;
    Intent batteryStatus = getBatteryStatusIntent();
    if (batteryStatus != null) {
      temp = (float) (batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0);
    }
    return temp;
  }

  /**
   * Gets battery voltage.
   *
   * @return the battery voltage
   */
  public final int getBatteryVoltage() {
    int volt = 0;
    Intent batteryStatus = getBatteryStatusIntent();
    if (batteryStatus != null) {
      volt = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
    }
    return volt;
  }

  /**
   * Gets charging source.
   *
   * @return the charging source
   */
  @ChargingVia
  public final int getChargingSource() {
    Intent batteryStatus = getBatteryStatusIntent();
    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

    switch (chargePlug) {
      case BatteryManager.BATTERY_PLUGGED_AC:
        return ChargingVia.AC;
      case BatteryManager.BATTERY_PLUGGED_USB:
        return ChargingVia.USB;
      case BatteryManager.BATTERY_PLUGGED_WIRELESS:
        return ChargingVia.WIRELESS;
      default:
        return ChargingVia.UNKNOWN_SOURCE;
    }
  }

  /**
   * Is battery present boolean.
   *
   * @return the boolean
   */
  public final boolean isBatteryPresent() {
    return getBatteryStatusIntent().getExtras() != null && getBatteryStatusIntent().getExtras()
        .getBoolean(BatteryManager.EXTRA_PRESENT);
  }
}