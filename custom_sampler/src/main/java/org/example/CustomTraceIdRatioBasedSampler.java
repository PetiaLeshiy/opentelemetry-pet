package org.example;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.internal.OtelEncodingUtils;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomTraceIdRatioBasedSampler implements Sampler {

    private static final SamplingResult POSITIVE_SAMPLING_RESULT = SamplingResult.recordAndSample();

    private static final SamplingResult NEGATIVE_SAMPLING_RESULT = SamplingResult.drop();

    private final long idUpperBound;
//    private final String description;

    static CustomTraceIdRatioBasedSampler create(double ratio) {
        if (ratio < 0.0 || ratio > 1.0) {
            throw new IllegalArgumentException("ratio must be in range [0.0, 1.0]");
        }
        long idUpperBound;
        // Special case the limits, to avoid any possible issues with lack of precision across
        // double/long boundaries. For probability == 0.0, we use Long.MIN_VALUE as this guarantees
        // that we will never sample a trace, even in the case where the id == Long.MIN_VALUE, since
        // Math.Abs(Long.MIN_VALUE) == Long.MIN_VALUE.
        if (ratio == 0.0) {
            idUpperBound = Long.MIN_VALUE;
        } else if (ratio == 1.0) {
            idUpperBound = Long.MAX_VALUE;
        } else {
            idUpperBound = (long) (ratio * Long.MAX_VALUE);
        }
        return new CustomTraceIdRatioBasedSampler(idUpperBound);
    }

    CustomTraceIdRatioBasedSampler(long idUpperBound) {
        this.idUpperBound = idUpperBound;
//        description = "CustomTraceIdRatioBasedSampler";
    }

    @Override
    public SamplingResult shouldSample(
            Context parentContext,
            String traceId,
            String name,
            SpanKind spanKind,
            Attributes attributes,
            List<LinkData> parentLinks) {
        // Always sample if we are within probability range. This is true even for child spans (that
        // may have had a different sampling samplingResult made) to allow for different sampling
        // policies,
        // and dynamic increases to sampling probabilities for debugging purposes.
        // Note use of '<' for comparison. This ensures that we never sample for probability == 0.0,
        // while allowing for a (very) small chance of *not* sampling if the id == Long.MAX_VALUE.
        // This is considered a reasonable tradeoff for the simplicity/performance requirements (this
        // code is executed in-line for every Span creation).

        Map<AttributeKey<?>, Object> map = attributes.asMap();
        System.out.println("+++++++++ ATTRIBUTES MAP: " + map);
        for (Map.Entry<AttributeKey<?>, Object> entry : map.entrySet()) {
            System.out.println("+++++++ entry: " + entry);
            if (entry.getKey().getKey().equals("number")) {
                return POSITIVE_SAMPLING_RESULT;
            }
        }
        return Math.abs(getTraceIdRandomPart(traceId)) < idUpperBound
                ? POSITIVE_SAMPLING_RESULT
                : NEGATIVE_SAMPLING_RESULT;
    }

    @Override
    public String getDescription() {
        return "CustomTraceIdRatioBasedSampler";
    }

//    @Override
//    public boolean equals(@Nullable Object obj) {
//        if (!(obj instanceof CustomTraceIdRatioBasedSampler)) {
//            return false;
//        }
//        CustomTraceIdRatioBasedSampler that = (CustomTraceIdRatioBasedSampler) obj;
//        return idUpperBound == that.idUpperBound;
//    }
//
//    @Override
//    public int hashCode() {
//        return Long.hashCode(idUpperBound);
//    }
//
//    @Override
//    public String toString() {
//        return getDescription();
//    }

//    // Visible for testing
//    long getIdUpperBound() {
//        return idUpperBound;
//    }

    private static long getTraceIdRandomPart(String traceId) {
        return OtelEncodingUtils.longFromBase16String(traceId, 16);
    }

//    private static String decimalFormat(double value) {
//        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ROOT);
//        decimalFormatSymbols.setDecimalSeparator('.');
//
//        DecimalFormat decimalFormat = new DecimalFormat("0.000000", decimalFormatSymbols);
//        return decimalFormat.format(value);
//    }
}
