package org.cyclops.commoncapabilities.ingredient.storage;

import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerConcatenate;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.fluidhandler.FluidMatch;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestFluidStackComponentStorageWrapper {

    private static FluidStack WATER_1;
    private static FluidStack LAVA_1_NB;
    private static FluidStack LAVA_1;
    private static FluidStack LAVA_10;
    private static FluidStack LAVA_9;
    private static FluidStack WATER_10;

    private static FluidStack WATER_64;
    private static FluidStack LAVA_64;
    private static FluidStack LAVA_64_NB;
    private static FluidStack WATER_11;
    private static FluidStack LAVA_11_NB;
    private static FluidStack WATER_2;
    private static FluidStack WATER_9;

    private IFluidHandler storage;
    private IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper wrapper;
    private FluidTank t1;
    private FluidTank t2;
    private FluidTank t3;
    private FluidTank t4;

    @BeforeClass
    public static void init() {
        // We need the Minecraft registries to be filled
        Bootstrap.register();
    }

    public static boolean eq(FluidStack a, FluidStack b) {
        return IngredientComponents.FLUIDSTACK.getMatcher().matchesExactly(a, b);
    }

    @Before
    public void beforeEach() {
        WATER_1 = new FluidStack(FluidRegistry.WATER, 1);
        LAVA_1_NB = new FluidStack(FluidRegistry.LAVA, 1, new NBTTagCompound());
        LAVA_1 = new FluidStack(FluidRegistry.LAVA, 1);
        LAVA_10 = new FluidStack(FluidRegistry.LAVA, 10);
        LAVA_9 = new FluidStack(FluidRegistry.LAVA, 9);
        WATER_10 = new FluidStack(FluidRegistry.WATER, 10);

        WATER_64 = new FluidStack(FluidRegistry.WATER, 64);
        LAVA_64 = new FluidStack(FluidRegistry.LAVA, 64);
        LAVA_64_NB = new FluidStack(FluidRegistry.LAVA, 64, new NBTTagCompound());
        WATER_11 = new FluidStack(FluidRegistry.WATER, 11);
        LAVA_11_NB = new FluidStack(FluidRegistry.LAVA, 11, new NBTTagCompound());
        WATER_2 = new FluidStack(FluidRegistry.WATER, 2);
        WATER_9 = new FluidStack(FluidRegistry.WATER, 9);

        t1 = new FluidTank(64);
        t2 = new FluidTank(64);
        t3 = new FluidTank(64);
        t4 = new FluidTank(64);
        storage = new FluidHandlerConcatenate(
                new FluidTank(64),
                new FluidTank(64),
                t1,
                new FluidTank(64),
                t2,
                new FluidTank(64),
                t3,
                new FluidTank(64),
                t4,
                new FluidTank(64)
        );
        t1.fill(WATER_1.copy(), true);
        t2.fill(LAVA_1_NB.copy(), true);
        t3.fill(LAVA_10.copy(), true);
        t4.fill(WATER_10.copy(), true);
        wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, storage);
    }

    @Test
    public void testGetComponent() {
        assertThat(wrapper.getComponent(), is(IngredientComponents.FLUIDSTACK));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(wrapper.getMaxQuantity(), is(640L));
    }

    @Test
    public void testIterator() {
        assertThat(new IngredientLinkedList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator()),
                is(new IngredientLinkedList<>(IngredientComponents.FLUIDSTACK,
                        new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, null, null, WATER_1, null, LAVA_1_NB,
                                null, LAVA_10, null, WATER_10, null))));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_1, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_1)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_1_NB, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_1_NB)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_10, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_10, FluidMatch.EXACT)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_1, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_1, WATER_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_1_NB, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_1_NB, LAVA_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(LAVA_10, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, LAVA_1_NB, LAVA_10)));
        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_10, FluidMatch.FLUID)), is(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, WATER_1, WATER_10)));

        assertThat(new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, wrapper.iterator(WATER_10, FluidMatch.ANY)), is(
                new IngredientArrayList<>(IngredientComponents.FLUIDSTACK, null, null, WATER_1, null, LAVA_1_NB,
                        null, LAVA_10, null, WATER_10, null)));
    }

    @Test
    public void testInsert() {
        assertThat(wrapper.insert(WATER_64, true), nullValue());
        assertThat(storage.getTankProperties()[0].getContents(), nullValue());
        assertThat(wrapper.insert(WATER_64, true), nullValue());
        assertThat(storage.getTankProperties()[0].getContents(), nullValue());
        assertThat(wrapper.insert(WATER_64, true), nullValue());
        assertThat(storage.getTankProperties()[0].getContents(), nullValue());
        assertThat(wrapper.insert(LAVA_64, true), nullValue());
        assertThat(storage.getTankProperties()[0].getContents(), nullValue());
        assertThat(wrapper.insert(LAVA_64, true), nullValue());
        assertThat(storage.getTankProperties()[0].getContents(), nullValue());
        assertThat(wrapper.insert(WATER_64, false), nullValue());
        assertThat(eq(storage.getTankProperties()[0].getContents(), WATER_64), is(true));
        assertThat(storage.getTankProperties()[1].getContents(), nullValue());
        assertThat(wrapper.insert(WATER_64, false), nullValue());
        assertThat(eq(storage.getTankProperties()[0].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[1].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(storage.getTankProperties()[3].getContents(), nullValue());
        assertThat(wrapper.insert(WATER_64, false), nullValue());
        assertThat(eq(storage.getTankProperties()[0].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[1].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[3].getContents(), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(storage.getTankProperties()[5].getContents(), nullValue());
        assertThat(wrapper.insert(LAVA_64_NB, false), nullValue());
        assertThat(eq(storage.getTankProperties()[0].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[1].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[3].getContents(), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_64_NB), is(true));
        assertThat(eq(storage.getTankProperties()[5].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(storage.getTankProperties()[7].getContents(), nullValue());
        assertThat(wrapper.insert(LAVA_64_NB, false), nullValue());
        assertThat(eq(storage.getTankProperties()[0].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[1].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_64), is(true));
        assertThat(eq(storage.getTankProperties()[3].getContents(), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_64_NB), is(true));
        assertThat(eq(storage.getTankProperties()[5].getContents(), LAVA_64_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[7].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(storage.getTankProperties()[9].getContents(), nullValue());
    }

    @Test
    public void testInsertFull() {
        IFluidHandler storage = new FluidTank(0);
        IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, storage);
        assertThat(eq(wrapper.insert(WATER_64, true), WATER_64), is(true));
    }

    @Test
    public void testExtractAny() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.ANY, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.ANY, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.ANY, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.ANY, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.ANY, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.ANY, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.ANY, false), LAVA_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_9), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
    }

    @Test
    public void testExtractAmount() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT, false), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(WATER_11, FluidMatch.AMOUNT, false), null), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT, false), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT, false), null), is(true));
    }

    @Test
    public void testExtractFluid() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID, false), LAVA_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_9), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
    }

    @Test
    public void testExtractNbt() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.NBT, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.NBT, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.NBT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.NBT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.NBT, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.NBT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.NBT, false), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
    }

    @Test
    public void testExtractFluidAmount() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.AMOUNT, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.AMOUNT, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID | FluidMatch.AMOUNT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.AMOUNT, true), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.AMOUNT, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.AMOUNT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.AMOUNT, false), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID | FluidMatch.AMOUNT, false), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
    }

    @Test
    public void testExtractFluidNbt() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.NBT, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.NBT, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.FLUID | FluidMatch.NBT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.NBT, true), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.FLUID | FluidMatch.NBT, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.FLUID | FluidMatch.NBT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.FLUID | FluidMatch.NBT, false), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
    }

    @Test
    public void testExtractAmountNbt() {
        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT | FluidMatch.NBT, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT | FluidMatch.NBT, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT | FluidMatch.NBT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT | FluidMatch.NBT, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_1, FluidMatch.AMOUNT | FluidMatch.NBT, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.AMOUNT | FluidMatch.NBT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(WATER_10, FluidMatch.AMOUNT | FluidMatch.NBT, false), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_10), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.AMOUNT | FluidMatch.NBT, false), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
    }

    @Test
    public void testExtractExact() {
        assertThat(eq(wrapper.extract(WATER_9, FluidMatch.EXACT, true), WATER_9), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(WATER_2, FluidMatch.EXACT, true), WATER_2), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.EXACT, true), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.EXACT, true), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(WATER_9, FluidMatch.EXACT, false), WATER_9), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_2), is(true));
        assertThat(eq(wrapper.extract(WATER_2, FluidMatch.EXACT, false), WATER_2), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
        assertThat(eq(wrapper.extract(LAVA_1_NB, FluidMatch.EXACT, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.EXACT, false), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
    }

    @Test
    public void testExtractNoExtract() {
        IFluidHandler storage = new FluidTank(1) {
            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                return null;
            }
        };
        storage.fill(LAVA_10, true);
        IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper wrapper = new IngredientComponentStorageWrapperHandlerFluidStack.ComponentStorageWrapper(IngredientComponents.FLUIDSTACK, storage);
        assertThat(eq(wrapper.extract(LAVA_10, FluidMatch.EXACT, false), null), is(true));
    }

    @Test
    public void testExtractMax() {
        assertThat(eq(wrapper.extract(10, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(1, true), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, true), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), WATER_1), is(true));

        assertThat(eq(wrapper.extract(10, false), WATER_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(1, false), LAVA_1_NB), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, false), LAVA_10), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), WATER_1), is(true));
        assertThat(eq(wrapper.extract(10, false), WATER_1), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
        assertThat(eq(wrapper.extract(10, false), null), is(true));
        assertThat(eq(storage.getTankProperties()[2].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[4].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[6].getContents(), null), is(true));
        assertThat(eq(storage.getTankProperties()[8].getContents(), null), is(true));
    }

}
