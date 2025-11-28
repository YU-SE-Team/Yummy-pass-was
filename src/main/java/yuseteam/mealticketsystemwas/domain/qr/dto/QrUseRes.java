package yuseteam.mealticketsystemwas.domain.qr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QrUseRes {

    private Long ticketId;
    private String message;
}


