package fuzs.vehicleupgrade.config;

import net.minecraft.client.gui.screens.Screen;

public enum InventorySwitch {
    NONE {
        @Override
        public boolean isActive() {
            return false;
        }
    },
    DOUBLE_TAB {
        private int inventoryTriggerTime;

        @Override
        public boolean isActive() {
            return this.inventoryTriggerTime > 0;
        }

        @Override
        public void tick() {
            if (this.inventoryTriggerTime > 0) {
                this.inventoryTriggerTime--;
            }
        }

        @Override
        public void reset() {
            this.inventoryTriggerTime = 7;
        }
    },
    CONTROL {
        @Override
        public boolean isActive() {
            return Screen.hasControlDown();
        }
    };

    public abstract boolean isActive();

    public void tick() {
        // NO-OP
    }

    public void reset() {
        // NO-OP
    }
}
