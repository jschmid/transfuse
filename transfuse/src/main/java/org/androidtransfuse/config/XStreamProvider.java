/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.model.manifest.*;
import org.androidtransfuse.processor.MergeableTagConverter;

import javax.inject.Provider;
import java.io.Writer;

/**
 * @author John Ericksen
 */
public class XStreamProvider implements Provider<XStream> {

    @Override
    public XStream get() {
        XStream xStream = new XStream(new FourSpaceTabXppDriver());

        xStream.processAnnotations(Manifest.class);

        xStream.registerConverter(new LabeledConverter<ConfigChanges>(ConfigChanges.class, ConfigChanges.values()));
        xStream.registerConverter(new LabeledConverter<InstallLocation>(InstallLocation.class, InstallLocation.values()));
        xStream.registerConverter(new LabeledConverter<LaunchMode>(LaunchMode.class, LaunchMode.values()));
        xStream.registerConverter(new LabeledConverter<ProtectionLevel>(ProtectionLevel.class, ProtectionLevel.values()));
        xStream.registerConverter(new LabeledConverter<ReqKeyboardType>(ReqKeyboardType.class, ReqKeyboardType.values()));
        xStream.registerConverter(new LabeledConverter<ReqNavigation>(ReqNavigation.class, ReqNavigation.values()));
        xStream.registerConverter(new LabeledConverter<ReqTouchScreen>(ReqTouchScreen.class, ReqTouchScreen.values()));
        xStream.registerConverter(new LabeledConverter<ScreenDensity>(ScreenDensity.class, ScreenDensity.values()));
        xStream.registerConverter(new LabeledConverter<ScreenOrientation>(ScreenOrientation.class, ScreenOrientation.values()));
        xStream.registerConverter(new LabeledConverter<ScreenSize>(ScreenSize.class, ScreenSize.values()));
        xStream.registerConverter(new LabeledConverter<UIOptions>(UIOptions.class, UIOptions.values()));
        xStream.registerConverter(new LabeledConverter<WindowSoftInputMode>(WindowSoftInputMode.class, WindowSoftInputMode.values()));
        xStream.registerConverter(new MergeableTagConverter());

        return xStream;
    }

    private static final class FourSpaceTabXppDriver extends XppDriver {

        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out, PrettyPrintWriter.XML_QUIRKS, "    ".toCharArray(), super.getNameCoder());
        }
    }
}
