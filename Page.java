public class Page {
    String addr;
    int bit, dirty;

    public Page(String a, int b) {
        addr = a;
        bit = b;
        dirty = 0;
    }

    public Page(String a, int r, int d) {
        addr = a;
        bit = r;
        dirty = d;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getBit() {
        return bit;
    }

    public void setBit(int bit) {
        this.bit = bit;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }


}
