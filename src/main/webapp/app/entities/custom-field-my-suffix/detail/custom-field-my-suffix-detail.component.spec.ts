import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomFieldMySuffixDetailComponent } from './custom-field-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('CustomFieldMySuffix Management Detail Component', () => {
    let comp: CustomFieldMySuffixDetailComponent;
    let fixture: ComponentFixture<CustomFieldMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CustomFieldMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ customField: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CustomFieldMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomFieldMySuffixDetailComponent);
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
