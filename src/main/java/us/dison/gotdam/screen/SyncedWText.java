package us.dison.gotdam.screen;

import io.github.cottonmc.cotton.gui.widget.WText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import us.dison.gotdam.scan.ScanStatus;

public class SyncedWText extends WText {

    private int field;
    private Text baseText;

    private boolean fieldIsScanResult = false;

    public SyncedWText(Text text, int field) {
        super(text);
        this.field = field;
        this.baseText = text;
    }

    public SyncedWText(Text text, int color, int field) {
        super(text, color);
        this.field = field;
        this.baseText = text;
    }

    public void forScanResult() {
        this.fieldIsScanResult = true;
    }

    @Override
    public void tick() {
        super.tick();
        try {
            MutableText t = baseText.copyContentOnly();
            if (fieldIsScanResult) {
                ScanStatus result = ScanStatus.fromOrdinal(host.getPropertyDelegate().get(field));
                setText(t.append(result.getMessage()));
            } else {
                setText(t.append(host.getPropertyDelegate().get(field) + ""));
            }
        } catch (NullPointerException ignored) {}
    }
}
