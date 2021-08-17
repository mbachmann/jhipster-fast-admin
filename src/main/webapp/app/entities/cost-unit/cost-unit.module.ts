import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CostUnitComponent } from './list/cost-unit.component';
import { CostUnitDetailComponent } from './detail/cost-unit-detail.component';
import { CostUnitUpdateComponent } from './update/cost-unit-update.component';
import { CostUnitDeleteDialogComponent } from './delete/cost-unit-delete-dialog.component';
import { CostUnitRoutingModule } from './route/cost-unit-routing.module';

@NgModule({
  imports: [SharedModule, CostUnitRoutingModule],
  declarations: [CostUnitComponent, CostUnitDetailComponent, CostUnitUpdateComponent, CostUnitDeleteDialogComponent],
  entryComponents: [CostUnitDeleteDialogComponent],
})
export class CostUnitModule {}
