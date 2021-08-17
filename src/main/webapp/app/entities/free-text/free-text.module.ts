import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FreeTextComponent } from './list/free-text.component';
import { FreeTextDetailComponent } from './detail/free-text-detail.component';
import { FreeTextUpdateComponent } from './update/free-text-update.component';
import { FreeTextDeleteDialogComponent } from './delete/free-text-delete-dialog.component';
import { FreeTextRoutingModule } from './route/free-text-routing.module';

@NgModule({
  imports: [SharedModule, FreeTextRoutingModule],
  declarations: [FreeTextComponent, FreeTextDetailComponent, FreeTextUpdateComponent, FreeTextDeleteDialogComponent],
  entryComponents: [FreeTextDeleteDialogComponent],
})
export class FreeTextModule {}
