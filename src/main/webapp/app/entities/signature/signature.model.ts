import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface ISignature {
  id?: number;
  remoteId?: number | null;
  signatureImageContentType?: string | null;
  signatureImage?: string | null;
  width?: number | null;
  heigth?: number | null;
  userName?: string | null;
  applicationUser?: IApplicationUser | null;
}

export class Signature implements ISignature {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public signatureImageContentType?: string | null,
    public signatureImage?: string | null,
    public width?: number | null,
    public heigth?: number | null,
    public userName?: string | null,
    public applicationUser?: IApplicationUser | null
  ) {}
}

export function getSignatureIdentifier(signature: ISignature): number | undefined {
  return signature.id;
}
