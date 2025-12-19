package fuzs.vehicleupgrade.config;

import fuzs.puzzleslib.api.core.v1.Proxy;

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

        @Override
        public void stop() {
            this.inventoryTriggerTime = 0;
        }
    },
    CONTROL {
        @Override
        public boolean isActive() {
            return Proxy.INSTANCE.hasControlDown();
        }
    };

    public abstract boolean isActive();

    public void tick() {
        // NO-OP
    }

    public void reset() {
        // NO-OP
    }

    public void stop() {
        // NO-OP
    }
}
