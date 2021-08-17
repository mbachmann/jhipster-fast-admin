import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LayoutFaComponent } from './list/layout-fa.component';
import { LayoutFaDetailComponent } from './detail/layout-fa-detail.component';
import { LayoutFaUpdateComponent } from './update/layout-fa-update.component';
import { LayoutFaDeleteDialogComponent } from './delete/layout-fa-delete-dialog.component';
import { LayoutFaRoutingModule } from './route/layout-fa-routing.module';

@NgModule({
  imports: [SharedModule, LayoutFaRoutingModule],
  declarations: [LayoutFaComponent, LayoutFaDetailComponent, LayoutFaUpdateComponent, LayoutFaDeleteDialogComponent],
  entryComponents: [LayoutFaDeleteDialogComponent],
})
export class LayoutFaModule {}
