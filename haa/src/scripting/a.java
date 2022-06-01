

package scripting;

final class a implements Runnable
{
    private /* synthetic */ int cx;
    private /* synthetic */ int cz;
    private /* synthetic */ AbstractPlayerInteraction a;
    
    a(final AbstractPlayerInteraction a, final int cx, final int cz) {
        this.a = a;
        this.cx = cx;
        this.cz = cz;
    }
    
    @Override
    public final void run() {
        if (this.a.getPlayer() != null && this.a.getPlayer().getMapId() == this.cx) {
            this.a.warp(this.cz, 0);
        }
    }
}
