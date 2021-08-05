import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomFieldFaDetailComponent } from './custom-field-fa-detail.component';

describe('Component Tests', () => {
  describe('CustomFieldFa Management Detail Component', () => {
    let comp: CustomFieldFaDetailComponent;
    let fixture: ComponentFixture<CustomFieldFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CustomFieldFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ customField: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CustomFieldFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomFieldFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load customField on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.customField).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
