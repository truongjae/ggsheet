import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;


import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SheetsQuickstart {
    private static final String APPLICATION_NAME = "sheetapi";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(in).createScoped(SCOPES);
        Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }

    public static void getSheetValue(String spreadsheetId, String range) throws GeneralSecurityException, IOException {
        Sheets service = getSheetsService();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                System.out.println(row.get(0)+","+ row.get(1));
            }
        }
    }
    public static void setSheetValue(String spreadsheetId, String range, String... values) throws GeneralSecurityException, IOException {
        Sheets service = getSheetsService();
        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(values)
                ));
        AppendValuesResponse appendValuesResponse = service.spreadsheets().values()
                .append(spreadsheetId,range,appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();
    }
    public static void updateSheetValue(String spreadsheetId, String column, String newValue) throws GeneralSecurityException, IOException {
        Sheets service = getSheetsService();
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(newValue)
                ));
        UpdateValuesResponse updateValuesResponse = service.spreadsheets().values()
                .update(spreadsheetId,column,body)
                .setValueInputOption("RAW")
                .execute();
    }
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        final String spreadsheetId = "1IITwIsVAYiG41cFViAW-g0Mwef4SBvnTLJlzzq3ueHo";
        final String range = "demosondx";
//        getSheetValue(spreadsheetId,range);
//        setSheetValue(spreadsheetId,range,"Nguyen van a","nu");
        updateSheetValue(spreadsheetId,"B4","nam");
    }
}