import { IApplicationUserFa } from 'app/entities/application-user-fa/application-user-fa.model';

export interface ISignatureFa {
  id?: number;
  remoteId?: number | null;
  signatureImageContentType?: string | null;
  signatureImage?: string | null;
  width?: number | null;
  heigth?: number | null;
  userName?: string | null;
  applicationUser?: IApplicationUserFa | null;
}

export class SignatureFa implements ISignatureFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public signatureImageContentType?: string | null,
    public signatureImage?: string | null,
    public width?: number | null,
    public heigth?: number | null,
    public userName?: string | null,
    public applicationUser?: IApplicationUserFa | null
  ) {}
}

export function getSignatureFaIdentifier(signature: ISignatureFa): number | undefined {
  return signature.id;
}
