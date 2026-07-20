package baguchi.spiced_twilight.data;

import baguchi.spiced_twilight.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class CraftingGenerator extends RecipeProvider {
    public CraftingGenerator(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> p_323846_) {
        super(p_248933_, p_323846_);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CINDER_POUCH.get(), 1)
                .pattern("HFH")
                .pattern(" H ")
                .define('H', ModItems.HYDRA_HIDE)
                .define('F', ModItems.FIRE_BEETLE_POWDER)
                .unlockedBy("has_item", has(ModItems.HYDRA_HIDE))
                .save(consumer);
    }
}
