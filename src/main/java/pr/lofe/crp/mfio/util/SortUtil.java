package pr.lofe.crp.mfio.util;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SortUtil {

    @Contract(pure = true)
    public static @NotNull List<String> searchFor(@Nullable String input, @NotNull String... strings) {
        return search(List.of(strings), input);
    }

    @Contract(pure = true)
    public static @NotNull List<String> search(@NotNull List<String> values, @Nullable String input) {
        List<String> output = new ArrayList<>();
        boolean deep = values.size() > 100;
        if(input == null)
            output = List.copyOf(values);
        else {
            for (String value : values) {
                boolean similar = true;
                if(input.length() > value.length())
                    similar = false;
                else {
                    for (int i = 0; i < input.length(); i++) {
                        if (value.charAt(i) != input.charAt(i)) {
                            similar = false;
                            break;
                        }
                    }
                }
                if (similar) output.add(value);
                else if (deep && value.contains(input)) output.add(value);
            }
        }
        return Lists.newArrayList(output);
    }

}
