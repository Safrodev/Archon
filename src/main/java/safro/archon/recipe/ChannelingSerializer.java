package safro.archon.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class ChannelingSerializer implements RecipeSerializer<ChannelingRecipe> {

    @Override
    public ChannelingRecipe read(Identifier id, JsonObject json) {
        if (isTag(json)) {
            Identifier block = new Identifier(JsonHelper.getString(json, "tag"));
            TagKey<Block> tag = TagKey.of(Registry.BLOCK_KEY, block);
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            int manaCost = readCost(json);
            return new ChannelingRecipe(tag, output, manaCost, id);
        } else {
            Block input = Registry.BLOCK.get(new Identifier(JsonHelper.getString(json, "block")));
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            int manaCost = readCost(json);
            return new ChannelingRecipe(input, output, manaCost, id);
        }
    }

    @Override
    public ChannelingRecipe read(Identifier id, PacketByteBuf buf) {
        Block input = Registry.BLOCK.get(new Identifier(buf.readString()));
        ItemStack stack = buf.readItemStack();
        int cost = buf.readInt();
        return new ChannelingRecipe(input, stack, cost, id);
    }

    @Override
    public void write(PacketByteBuf buf, ChannelingRecipe recipe) {
        buf.writeString(Registry.BLOCK.getId(recipe.getBlock()).toString());
        buf.writeItemStack(recipe.getOutput());
        buf.writeInt(recipe.getManaCost());
    }

    public int readCost(JsonObject json) {
        int i = JsonHelper.getInt(json, "manaCost", 1);
        if (i < 1) {
            throw new JsonSyntaxException("Invalid mana cost: " + i);
        }
        return i;
    }

    public static boolean isTag(JsonObject json) {
        if (json.has("block") && json.has("tag")) {
            throw new JsonParseException("Channeling recipe must container either a tag or a block, not both");
        } else if (json.has("block")) {
            return false;
        } else if (json.has("tag")) {
            return true;
        } else {
            throw new JsonParseException("A channeling recipe needs either a tag or a block");
        }
    }
}
