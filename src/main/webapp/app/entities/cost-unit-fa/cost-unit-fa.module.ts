import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CostUnitFaComponent } from './list/cost-unit-fa.component';
import { CostUnitFaDetailComponent } from './detail/cost-unit-fa-detail.component';
import { CostUnitFaUpdateComponent } from './update/cost-unit-fa-update.component';
import { CostUnitFaDeleteDialogComponent } from './delete/cost-unit-fa-delete-dialog.component';
import { CostUnitFaRoutingModule } from './route/cost-unit-fa-routing.module';

@NgModule({
  imports: [SharedModule, CostUnitFaRoutingModule],
  declarations: [CostUnitFaComponent, CostUnitFaDetailComponent, CostUnitFaUpdateComponent, CostUnitFaDeleteDialogComponent],
  entryComponents: [CostUnitFaDeleteDialogComponent],
})
export class CostUnitFaModule {}
