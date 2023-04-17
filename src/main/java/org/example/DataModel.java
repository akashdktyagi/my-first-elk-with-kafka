package org.example;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DataModel {

    String projectID;
    Integer testCount;
    Integer testFailed;
    String sha;
    Integer coverage;
    Integer vulnerabilities;
    Integer sastCritical;
    Integer sastMedium;
    Integer sastLow;




}
