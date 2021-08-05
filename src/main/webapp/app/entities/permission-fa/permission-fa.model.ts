import { IRoleFa } from 'app/entities/role-fa/role-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { PermissionType } from 'app/entities/enumerations/permission-type.model';
import { DomainArea } from 'app/entities/enumerations/domain-area.model';

export interface IPermissionFa {
  id?: number;
  add?: PermissionType;
  edit?: PermissionType;
  manage?: PermissionType;
  domainArea?: DomainArea;
  role?: IRoleFa | null;
  contact?: IContactFa | null;
}

export class PermissionFa implements IPermissionFa {
  constructor(
    public id?: number,
    public add?: PermissionType,
    public edit?: PermissionType,
    public manage?: PermissionType,
    public domainArea?: DomainArea,
    public role?: IRoleFa | null,
    public contact?: IContactFa | null
  ) {}
}

export function getPermissionFaIdentifier(permission: IPermissionFa): number | undefined {
  return permission.id;
}
