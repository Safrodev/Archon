package safro.archon.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ChannelingSerializer implements RecipeSerializer<ChannelingRecipe> {

    @Override
    public ChannelingRecipe read(Identifier id, JsonObject json) {
        if (isTag(json)) {
            Identifier block = new Identifier(JsonHelper.getString(json, "tag"));
            TagKey<Block> tag = TagKey.of(RegistryKeys.BLOCK, block);
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            int manaCost = readCost(json);
            return new ChannelingRecipe(tag, output, manaCost, id);
        } else if (json.has("block")) {
            Block input = Registries.BLOCK.get(new Identifier(JsonHelper.getString(json, "block")));
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            int manaCost = readCost(json);
            return new ChannelingRecipe(input, output, manaCost, id);
        } else {
            throw new JsonSyntaxException("Channeling Recipe must contain either an input tag or block.");
        }
    }

    @Override
    public ChannelingRecipe read(Identifier id, PacketByteBuf buf) {
        ItemStack stack = buf.readItemStack();
        int cost = buf.readInt();
        boolean tag = buf.readBoolean();
        if (tag) {
            TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, new Identifier(buf.readString()));
            return new ChannelingRecipe(tagKey, stack, cost, id);
        } else {
            Block input = Registries.BLOCK.get(new Identifier(buf.readString()));
            return new ChannelingRecipe(input, stack, cost, id);
        }
    }

    @Override
    public void write(PacketByteBuf buf, ChannelingRecipe recipe) {
        buf.writeItemStack(recipe.result);
        buf.writeInt(recipe.getManaCost());

        boolean tag = recipe.getTag() != null;
        buf.writeBoolean(tag);
        if (!tag) {
            String block = Registries.BLOCK.getId(recipe.getBlock()).toString();
            buf.writeString(block);
        } else {
            String tagId = recipe.getTag().id().toString();
            buf.writeString(tagId);
        }
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
