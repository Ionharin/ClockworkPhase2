package lumaceon.mods.clockworkphase2.client.gui;

import lumaceon.mods.clockworkphase2.api.block.clockwork.IClockworkNetworkMachine;
import lumaceon.mods.clockworkphase2.api.clockworknetwork.ClockworkNetworkContainer;
import lumaceon.mods.clockworkphase2.api.clockworknetwork.ClockworkNetworkGuiClient;
import lumaceon.mods.clockworkphase2.api.util.ClockworkNetwork;
import lumaceon.mods.clockworkphase2.client.gui.components.GuiButtonCNGui;
import lumaceon.mods.clockworkphase2.client.gui.components.GuiCNGuiElement;
import lumaceon.mods.clockworkphase2.container.ContainerClockworkController;
import lumaceon.mods.clockworkphase2.lib.Textures;
import lumaceon.mods.clockworkphase2.tile.clockwork.TileClockworkController;
import lumaceon.mods.clockworkphase2.util.ChildGuiData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiClockworkController extends GuiContainer
{
    private Minecraft mc = Minecraft.getMinecraft();
    private ScaledResolution resolution;
    private State guiState = State.DEFAULT;
    public TileClockworkController te;
    public InventoryPlayer ip;

    public ArrayList<ChildGuiData> guiDataList = new ArrayList<ChildGuiData>(8);
    private ArrayList<ChildGuiData> inactiveGUIs = new ArrayList<ChildGuiData>(8);
    private ClockworkNetwork cn;
    private int pageNumber = 0;

    private int mouseClickedAtX, mouseClickedAtY, selectionOriginX, selectionOriginY;
    private GuiCNGuiElement selectedConfigurationGui = null;

    //Fixes a bug where actionPerformed is called twice if the state changes and adds a button at the clicked location
    private boolean stateChangedThisTick = false;

    public GuiClockworkController(InventoryPlayer ip, TileClockworkController te, World world) {
        super(null);
        this.resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        this.inventorySlots = new ContainerClockworkController(ip, te, world, resolution.getScaledWidth(), resolution.getScaledHeight());
        this.te = te;
        this.ip = ip;
        this.cn = te.getClockworkNetwork();
        this.xSize = resolution.getScaledWidth();
        this.ySize = resolution.getScaledHeight();
    }

    @Override
    public void initGui()
    {
        resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        this.xSize = resolution.getScaledWidth();
        this.ySize = resolution.getScaledHeight();
        super.initGui();

        buttonList.clear();
        calculateActiveGUIs();
        if(guiState.equals(State.DEFAULT))
        {
            ((ContainerClockworkController)inventorySlots).reinitializeSlots(ip, guiDataList, guiLeft - 86 + xSize / 2, guiTop + ySize - 85, xSize, ySize, false);
            buttonList.add(new GuiButton(0, guiLeft - 125 + xSize / 2, guiTop + 4, 250, 20, "Configure GUI"));
        }
        else if(guiState.equals(State.CONFIG))
        {
            ((ContainerClockworkController)inventorySlots).reinitializeSlots(ip, guiDataList, 0, 0, xSize, ySize, true);
            buttonList.add(new GuiButton(0, guiLeft, guiTop, 60, 20, "Save Setup"));
            buttonList.add(new GuiButton(1, guiLeft + xSize - 20, guiTop, 20, 20, "+"));

            int iterations = 0;
            if(guiDataList != null && !guiDataList.isEmpty())
                for(ChildGuiData child : guiDataList)
                    if(child != null && child.gui != null && child.machine != null)
                    {
                        buttonList.add(new GuiCNGuiElement(child, 2+iterations, child.getX(xSize), child.getY(ySize), child.gui.getSizeX(), child.gui.getSizeY()));
                        ++iterations;
                    }
        }
        else if(guiState.equals(State.ADD_MACHINE))
        {
            ((ContainerClockworkController)inventorySlots).reinitializeSlots(ip, guiDataList, 0, 0, xSize, ySize, true);
            buttonList.add(new GuiButton(0, guiLeft - 30 + xSize / 2, guiTop, 60, 20, "Done"));
            buttonList.add(new GuiButton(1, guiLeft, guiTop, 20, 20, "<-"));
            buttonList.add(new GuiButton(2, guiLeft + xSize - 20, guiTop, 20, 20, "->"));

            int iterations = 0;
            if(inactiveGUIs != null && !inactiveGUIs.isEmpty())
                for(int n = pageNumber*4; n < inactiveGUIs.size() && n < (pageNumber+1) * 4; n++)
                {
                    ChildGuiData child = inactiveGUIs.get(n);
                    if(child != null)
                    {
                        buttonList.add(new GuiButtonCNGui(child.gui, 3+iterations, child.getX(xSize), child.getY(ySize), child.gui.getSizeX(), child.gui.getSizeY()));
                        ++iterations;
                    }
                }
        }
    }

    private void calculateActiveGUIs()
    {
        inactiveGUIs.clear();
        int iterations = 0;
        ArrayList<IClockworkNetworkMachine> machines = cn.getMachines();
        if(machines != null)
            for(IClockworkNetworkMachine machine : machines)
                if(machine != null)
                {
                    ClockworkNetworkContainer gui = machine.getGui();
                    if(gui != null)
                    {
                        boolean skip = false;
                        for(ChildGuiData child : guiDataList) //Don't list active GUIs as inactive.
                            if(child != null && child.machine != null && child.machine.equals(machine))
                            {
                                skip = true;
                                break;
                            }
                        if(skip)
                            continue;
                        int x = iterations % 2 == 0 ? guiLeft : xSize - gui.getSizeX();
                        int y = iterations % 4 >= 2 ? 160 : 40;
                        inactiveGUIs.add(new ChildGuiData(machine, gui, x, y, xSize, ySize));
                        ++iterations;
                    }
                }
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawScreen(x, y, partialTicks);
        if(stateChangedThisTick)
            stateChangedThisTick = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glEnable(GL11.GL_BLEND);
        drawStaticBackgrounds();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        if(guiState.equals(State.DEFAULT))
            for(ChildGuiData child : guiDataList)
                if(child.gui != null && child.gui instanceof ClockworkNetworkGuiClient)
                    ((ClockworkNetworkGuiClient) child.gui).drawBackground(guiLeft + child.getX(xSize), guiTop + child.getY(ySize), zLevel);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        if(guiState.equals(State.DEFAULT))
            for(ChildGuiData child : guiDataList)
                if(child.gui != null && child.gui instanceof ClockworkNetworkGuiClient)
                    ((ClockworkNetworkGuiClient) child.gui).drawForeground(guiLeft + child.getX(xSize), guiTop + child.getY(ySize), zLevel);
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        if(stateChangedThisTick)
            return;
        switch(guiState.ordinal())
        {
            case 0: //DEFAULT
                setGuiState(State.CONFIG);
                initGui();
                return;
            case 1: //CONFIG
                switch(button.id)
                {
                    case 0: //Save
                        setGuiState(State.DEFAULT);
                        initGui();
                        return;
                    case 1: //Add machine
                        setGuiState(State.ADD_MACHINE);
                        pageNumber = 0;
                        initGui();
                        return;
                }
                return;
            case 2: //ADD_MACHINE
                switch(button.id)
                {
                    case 0: //Done, back to config state.
                        setGuiState(State.CONFIG);
                        initGui();
                        return;
                    case 1: //Scroll left
                        --pageNumber;
                        if(pageNumber < 0)
                            pageNumber = (inactiveGUIs.size()-1) / 4;
                        initGui();
                        return;
                    case 2: //Scroll right
                        ++pageNumber;
                        if(pageNumber > (inactiveGUIs.size()-1) / 4)
                            pageNumber = 0;
                        initGui();
                        return;
                    default: //Add a machine by returning to config state and adding the gui.
                        setGuiState(State.CONFIG);
                        ChildGuiData child = inactiveGUIs.get(button.id - 3);
                        if(child == null || child.gui == null || child.machine == null)
                            return;
                        child.setLocation((xSize / 2) - child.gui.getSizeX() / 2, (ySize / 2) - 40 - child.gui.getSizeY() / 2, xSize, ySize);
                        guiDataList.add(child);
                        initGui();
                        break;
                }
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonID)
    {
        super.mouseClicked(x, y, buttonID);
        mouseClickedAtX = x;
        mouseClickedAtY = y;
        if(guiState.equals(State.CONFIG))
        {
            for(int n = buttonList.size() - 1; n > 1; n--) //Loop backwards from draw order to select the top gui first.
            {
                GuiButton button = (GuiButton) buttonList.get(n);
                if(button != null && button.mousePressed(mc, x, y) && button instanceof GuiCNGuiElement)
                {
                    this.selectedConfigurationGui = (GuiCNGuiElement) button;
                    selectionOriginX = button.xPosition;
                    selectionOriginY = button.yPosition;
                    break;
                }
            }
        }
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around.
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick)
    {
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
        if(guiState.equals(State.CONFIG))
            if(selectedConfigurationGui != null)
            {
                int newX = selectionOriginX + (mouseX - mouseClickedAtX);
                int newY = selectionOriginY + (mouseY - mouseClickedAtY);
                if(newX < guiLeft)
                    newX = guiLeft;
                if(newX > guiLeft + xSize - selectedConfigurationGui.width)
                    newX = guiLeft + xSize - selectedConfigurationGui.width;
                if(newY < guiTop)
                    newY = guiTop;
                if(newY > guiTop + ySize - selectedConfigurationGui.height)
                    newY = guiTop + ySize - selectedConfigurationGui.height;
                selectedConfigurationGui.moveGui(newX, newY, xSize, ySize);
            }
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        if(p_73869_2_ == 1 || p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode())
            this.mc.thePlayer.closeScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + p_73729_6_), (double)this.zLevel, 0, 1);
        tessellator.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + p_73729_6_), (double)this.zLevel, 1, 1);
        tessellator.addVertexWithUV((double) (p_73729_1_ + p_73729_5_), (double) (p_73729_2_ + 0), (double) this.zLevel, 1, 0);
        tessellator.addVertexWithUV((double) (p_73729_1_ + 0), (double) (p_73729_2_ + 0), (double) this.zLevel, 0, 0);
        tessellator.draw();
    }

    public State getGuiState() {
        return this.guiState;
    }

    public void setGuiState(State state) {
        this.stateChangedThisTick = true;
        this.guiState = state;
    }

    private void drawStaticBackgrounds()
    {
        boolean isDefault = guiState.equals(State.DEFAULT);
        if(!isDefault)
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
        else
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        //PLAYER INVENTORY
        mc.renderEngine.bindTexture(Textures.GUI.PLAYER_INVENTORY);
        this.drawTexturedModalRect(this.guiLeft - 86 + this.xSize / 2, this.guiTop + this.ySize - 85, 0, 0, 172, 85);
        //PLAYER INVENTORY

        //POWER METER TENSION
        mc.renderEngine.bindTexture(Textures.GUI.POWER_METER);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + this.ySize - 84, 0, 0, 84, 84);

        if(isDefault)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(guiLeft + 42F, guiTop + ySize - 42F, 0F);
            GL11.glRotatef(-134, 0F, 0F, 1F);
            GL11.glTranslatef(-guiLeft - 42F, -guiTop - ySize + 42F, 0F);
            mc.renderEngine.bindTexture(Textures.GUI.POWER_METER_HAND);
            this.drawTexturedModalRect(this.guiLeft, this.guiTop + this.ySize - 84, 0, 0, 84, 84);
            GL11.glPopMatrix();

            mc.renderEngine.bindTexture(Textures.GUI.POWER_METER_CENTER);
            this.drawTexturedModalRect(this.guiLeft, this.guiTop + this.ySize - 84, 0, 0, 84, 84);
        }
        //POWER METER TENSION

        //POWER METER TIME
        mc.renderEngine.bindTexture(Textures.GUI.POWER_METER);
        this.drawTexturedModalRect(this.guiLeft + this.xSize - 84, this.guiTop + this.ySize - 84, 0, 0, 84, 84);

        if(isDefault)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(guiLeft + xSize - 42F, guiTop + ySize - 42F, 0F);
            GL11.glRotatef(-134, 0F, 0F, 1F);
            GL11.glTranslatef(-guiLeft - xSize + 42F, -guiTop - ySize + 42F, 0F);
            mc.renderEngine.bindTexture(Textures.GUI.POWER_METER_HAND);
            this.drawTexturedModalRect(this.guiLeft + this.xSize - 84, this.guiTop + this.ySize - 84, 0, 0, 84, 84);
            GL11.glPopMatrix();

            mc.renderEngine.bindTexture(Textures.GUI.POWER_METER_CENTER);
            this.drawTexturedModalRect(this.guiLeft + this.xSize - 84, this.guiTop + this.ySize - 84, 0, 0, 84, 84);
        }
        //POWER METER TIME
    }

    public static enum State {
        DEFAULT, CONFIG, ADD_MACHINE
    }
}
