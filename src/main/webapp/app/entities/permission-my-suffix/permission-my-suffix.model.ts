import { IRoleMySuffix } from 'app/entities/role-my-suffix/role-my-suffix.model';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { IContactAddressMySuffix } from 'app/entities/contact-address-my-suffix/contact-address-my-suffix.model';
import { DomainResource } from 'app/entities/enumerations/domain-resource.model';

export interface IPermissionMySuffix {
  id?: number;
  newAll?: boolean;
  editOwn?: boolean;
  editAll?: boolean;
  viewOwn?: boolean;
  viewAll?: boolean;
  manageOwn?: boolean;
  manageAll?: boolean;
  domainResource?: DomainResource;
  role?: IRoleMySuffix | null;
  contact?: IContactMySuffix | null;
  contactAddress?: IContactAddressMySuffix | null;
}

export class PermissionMySuffix implements IPermissionMySuffix {
  constructor(
    public id?: number,
    public newAll?: boolean,
    public editOwn?: boolean,
    public editAll?: boolean,
    public viewOwn?: boolean,
    public viewAll?: boolean,
    public manageOwn?: boolean,
    public manageAll?: boolean,
    public domainResource?: DomainResource,
    public role?: IRoleMySuffix | null,
    public contact?: IContactMySuffix | null,
    public contactAddress?: IContactAddressMySuffix | null
  ) {
    this.newAll = this.newAll ?? false;
    this.editOwn = this.editOwn ?? false;
    this.editAll = this.editAll ?? false;
    this.viewOwn = this.viewOwn ?? false;
    this.viewAll = this.viewAll ?? false;
    this.manageOwn = this.manageOwn ?? false;
    this.manageAll = this.manageAll ?? false;
  }
}

export function getPermissionMySuffixIdentifier(permission: IPermissionMySuffix): number | undefined {
  return permission.id;
}
