import { ICatalogService } from 'app/entities/catalog-service/catalog-service.model';

export interface IReportingActivity {
  id?: number;
  remoteId?: number | null;
  name?: string | null;
  useServicePrice?: boolean | null;
  inactiv?: boolean | null;
  catalogService?: ICatalogService | null;
}

export class ReportingActivity implements IReportingActivity {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public name?: string | null,
    public useServicePrice?: boolean | null,
    public inactiv?: boolean | null,
    public catalogService?: ICatalogService | null
  ) {
    this.useServicePrice = this.useServicePrice ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getReportingActivityIdentifier(reportingActivity: IReportingActivity): number | undefined {
  return reportingActivity.id;
}
