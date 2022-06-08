package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;

public interface ICodecProvider {
    Codec<?> getCodec();
}
