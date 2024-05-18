package es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.ISBN;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class ISBNAnalyzer implements ImageAnalysis.Analyzer {

    ISBNScannerActivity isbnScannerActivity;
    public ISBNAnalyzer(ISBNScannerActivity isbnScannerActivity) {
        this.isbnScannerActivity = isbnScannerActivity;
    }

    @ExperimentalGetImage
    @Override
    public void analyze(ImageProxy imageProxy) {

        Image mediaImage = imageProxy.getImage();

        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).enableAllPotentialBarcodes()
                    .build();

            BarcodeScanner scanner = BarcodeScanning.getClient(options);
            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            imageProxy.close();
                            if (barcodes.isEmpty())
                                return;
                            Barcode barcode = barcodes.get(0);
                            switch (barcode.getValueType()) {
                                case Barcode.TYPE_ISBN:
                                    String isbn_code = barcode.getDisplayValue();
                                    isbnScannerActivity.returnISBN(isbn_code);
                                    break;
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            imageProxy.close();
                            Log.e("Exception", e.toString());
                        }
                    });

        }
    }


}
