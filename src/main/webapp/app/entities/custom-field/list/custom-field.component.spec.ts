import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CustomFieldService } from '../service/custom-field.service';

import { CustomFieldComponent } from './custom-field.component';

describe('Component Tests', () => {
  describe('CustomField Management Component', () => {
    let comp: CustomFieldComponent;
    let fixture: ComponentFixture<CustomFieldComponent>;
    let service: CustomFieldService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomFieldComponent],
      })
        .overrideTemplate(CustomFieldComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomFieldComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CustomFieldService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.customFields?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
