import { IRoleMySuffix } from 'app/entities/role-my-suffix/role-my-suffix.model';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { PermissionType } from 'app/entities/enumerations/permission-type.model';
import { DomainArea } from 'app/entities/enumerations/domain-area.model';

export interface IPermissionMySuffix {
  id?: number;
  add?: PermissionType;
  edit?: PermissionType;
  manage?: PermissionType;
  domainArea?: DomainArea;
  role?: IRoleMySuffix | null;
  contact?: IContactMySuffix | null;
}

export class PermissionMySuffix implements IPermissionMySuffix {
  constructor(
    public id?: number,
    public add?: PermissionType,
    public edit?: PermissionType,
    public manage?: PermissionType,
    public domainArea?: DomainArea,
    public role?: IRoleMySuffix | null,
    public contact?: IContactMySuffix | null
  ) {}
}

export function getPermissionMySuffixIdentifier(permission: IPermissionMySuffix): number | undefined {
  return permission.id;
}
