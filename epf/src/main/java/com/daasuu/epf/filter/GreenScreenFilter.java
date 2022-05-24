package com.daasuu.epf.filter;

public class GreenScreenFilter extends GlFilter {

    private static final String CHROMA_SHADER = "" +
            "precision mediump float;" +
            " varying vec2 vTextureCoord;\n" +
            "\n" +
            " uniform lowp sampler2D sTexture;\n" +
            " uniform lowp float vibrance;\n" +
            " uniform float smoothing;\n" +
            " uniform float thresholdSensitivity;\n" +
            "\n" +
            "void main() {\n" +
            "    lowp vec4 color = texture2D(sTexture, vTextureCoord);\n" +
            "    lowp float rbAverage = color.r * 0.5 + color.b * 0.5;\n" +
            "    lowp float gDelta = color.g - rbAverage;\n" +
            "    color.a = 1.0 - smoothstep(0.0, 0.25, gDelta);\n" +
            "    color.a = color.a * color.a * color.a;\n" +
            "    gl_FragColor = color;\n" +
            "}";
               /* "    lowp float average = (color.r + color.g + color.b) / 3.0;\n" +
                "    lowp float mx = max(color.r, max(color.g, color.b));\n" +
                "    lowp float amt = (mx - average) * (-(vibrance + smoothing + thresholdSensitivity) * 3.0);\n" +
                "    color.rgb = mix(color.rgb, vec3(mx), amt);\n" +
                "    gl_FragColor = color;\n" +
                "}"*/

    public GreenScreenFilter() {
        super(DEFAULT_VERTEX_SHADER, CHROMA_SHADER);
    }
}
